package lib.util;

import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

public class RxBus {

    private static volatile RxBus instance;
    private final Subject<Object> mSubject;
    private HashMap<String, CompositeDisposable> mSubscriptionMap;

    //单列模式
    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    public RxBus() {
        mSubject = PublishSubject.create().toSerialized();
    }

    public void post(Object o) {
        mSubject.onNext(o);
    }

    /**
     * 返回指定类型的带背压的Flowable实例
     *
     * @param <T>
     * @param type
     * @return
     */
    public <T> Flowable<T> toObservable(Class<T> type) {
        return mSubject.toFlowable(BackpressureStrategy.BUFFER)
                .ofType(type);
    }


    /**
     * 默认订阅方法
     *
     * @param o    填写this，否则无法自动取消订阅
     * @param type
     * @param next
     * @param <T>
     */
    public <T> void doSubscribe(Object o, Class<T> type, Consumer<T> next) {
        Disposable subscribe = toObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(next);
        addSubscription(o, subscribe);
    }

    /**
     * 订阅方法
     *
     * @param <T>
     * @param type
     * @param next
     * @param error
     * @return
     */
    public <T> void doSubscribe(Object o, Class<T> type, Consumer<T> next, Consumer<Throwable> error) {
        Disposable subscribe = toObservable(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidScheduler.mainThread())
                .subscribe(next, error);
        addSubscription(o, subscribe);
    }


    /**
     * 是否已有观察者订阅
     *
     * @return
     */
    public boolean hasObservers() {
        return mSubject.hasObservers();
    }

    /**
     * 保存订阅后的disposable
     *
     * @param o
     * @param disposable
     */
    private void addSubscription(Object o, Disposable disposable) {
        if (mSubscriptionMap == null) {
            mSubscriptionMap = new HashMap<>();
        }
        String key = o.getClass().getName();
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).add(disposable);
        } else {
            //一次性容器,可以持有多个并提供 添加和移除。
            CompositeDisposable disposables = new CompositeDisposable();
            disposables.add(disposable);
            mSubscriptionMap.put(key, disposables);
        }
    }

    /**
     * 取消订阅
     *
     * @param o
     */
    public void unSubscribe(Object o) {
        if (mSubscriptionMap == null) {
            return;
        }

        String key = o.getClass().getName();
        if (!mSubscriptionMap.containsKey(key)) {
            return;
        }
        if (mSubscriptionMap.get(key) != null) {
            mSubscriptionMap.get(key).dispose();
        }

        mSubscriptionMap.remove(key);
    }
}