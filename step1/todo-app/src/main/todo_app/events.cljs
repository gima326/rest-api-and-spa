(ns todo-app.events
  (:require [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [re-frame.core :as re-frame]
            [todo-app.config :as config]

            [todo-app.db :as db]))

(def request-defaults
  {:timeout 6000
   :response-format (ajax/json-response-format {:keywords? true})
   :on-failure [::set-error]
   })

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-current-route
 (fn [db [_ route]]
  (assoc db :route route)))

;; なんらかのエラーが発生した場合に、こちらに来るみたい。

(re-frame/reg-event-db
  ::set-error
  (fn [db [_ res]]
    (assoc db :error res)))

;; views から呼び出される。

(re-frame/reg-event-fx
  ::fetch-todos
  (fn [_ _]
    {:http-xhrio (assoc request-defaults
                        :method :get
                        :uri (str config/API_URL "/todos")
                        :on-success [::update-todos])}))

;; fetch-todos から呼び出される。

(re-frame/reg-event-db
 ::update-todos
 (fn [db [_ res]]
   (assoc db :todos res)))
