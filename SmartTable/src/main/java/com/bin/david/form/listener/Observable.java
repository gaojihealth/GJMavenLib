package com.bin.david.form.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2017/9/29.
 */

public abstract  class Observable<T> {

    public final ArrayList<T> observables = new ArrayList<>();

    /**
     * AttachObserver（通过实例注册观察者）
     * @param observer T
     **/
    public void register(T observer){
        if(observer==null) throw new NullPointerException();
        synchronized(observables){
            if(!observables.contains(observer)){
                observables.add(observer);
            }
        }
    }



    /**
     * UnattachObserver（注销观察者）
     * @param observer T
     **/
    public void unRegister(T observer){
        if(observer==null) throw new NullPointerException();
        if(observables.contains(observer)){
            observables.remove(observer);
        }
    }


    public void unRegisterAll(){
        synchronized(observables){
            observables.clear();
        }
    }

    /**
     * Ruturnthesizeofobservers
     * @return int
     */
    public int countObservers(){
        synchronized(observables){
            return observables.size();
        }
    }

    /**
     *notify all observer（通知所有观察者，在子类中实现）
     * @param observers List
     */
    public abstract void notifyObservers(List<T> observers);


}
