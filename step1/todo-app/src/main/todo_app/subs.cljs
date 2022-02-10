(ns todo-app.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::current-route
 (fn [db _]
   (get db :route {:handler :todo-app.views/home})))

(re-frame/reg-sub
  ::todos
  (fn [db _]
    (:todos db)))

(re-frame/reg-sub
 ::todo2
 (fn [db _]
   (:todos

    ;; 引数: db
    ;; この時点で、db にはこのような値が設定されていないといけない！
    {:todos
     (vals
      {1 {:id 1
          :task "build an API"}
       2 {:id 2
          :task "?????"}
       3 {:id 3
          :task "profit!"}})
     })
   ))
