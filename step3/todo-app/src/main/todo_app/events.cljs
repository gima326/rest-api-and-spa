(ns todo-app.events
  (:require [ajax.core :as ajax]
            [day8.re-frame.http-fx]
            [re-frame.core :as re-frame]
            [todo-app.config :as config]

            [todo-app.db :as db]
            [todo-app.fx :as fx]))

(def request-defaults
  {:timeout 6000
   :response-format (ajax/json-response-format {:keywords? true})
   :on-failure [::set-error]
   })

(defmulti on-navigate (fn [view _] view))

(defmethod on-navigate :todo-app.views/list [_ _]
  {:dispatch [::fetch-todos]})

(defmethod on-navigate :todo-app.views/edit [_ params]
  {:dispatch [::fetch-todo-by-id (:id params)]})

;; この「デフォルト」がないと、ルーティングが複数回まわるみたい。

(defmethod on-navigate :default [_ _] nil)

;; === db ===

(re-frame/reg-event-db
 ::initialize-db
 (fn  [_ _] db/default-db))

;; なんらかのエラーが発生した場合に、こちらに来るみたい。

(re-frame/reg-event-db
  ::set-error
  (fn [db [_ res]]
    (assoc db :error res)))

;; fetch-todos から呼び出される。

(re-frame/reg-event-db
 ::update-todos
 (fn [db [_ res]]
   (assoc db :todos res)))

(re-frame/reg-event-db
 ::set-todos
 (fn [db [_ res]]
   (assoc db
          :todos res
          :selected-todo nil)))

(re-frame/reg-event-db
 ::set-selected-todo
 (fn [db [_ res]]
   (assoc db :selected-todo
          ;;[{:id 1 :task "aiueo"}]
          res
          )))

(re-frame/reg-event-db
 ::open-delete-dialog
 (fn [db [_ todo-id]]
   (assoc db :delete-dialog {:todo-id todo-id})))

(re-frame/reg-event-db
 ::close-delete-dialog
 (fn [db _]
   (assoc db :delete-dialog nil)))


;; === fx ===

(;; re-frame/reg-event-db
 re-frame/reg-event-fx
 ::set-current-route

 ;; changed
 ;;(fn [db [_ route]] (assoc db :route route))
 (fn [{:keys [db]} [_ {:keys [handler route-params]
                       :as route}]]
   (merge {:db (assoc db :route route)}
          (on-navigate handler route-params))))

;; views から呼び出される。

(re-frame/reg-event-fx
 ::navigate
 (fn [_ [_ view params]]
   {::fx/navigate {:view view
                   :params params}}))

(re-frame/reg-event-fx
  ::fetch-todos
  (fn [_ _]
    {:http-xhrio (assoc request-defaults
                        :method :get
                        :uri (str config/API_URL "/todos")

                        ;; changed
                        ;;:on-success [::update-todos]
                        :on-success [::set-todos]
                        )}))

(re-frame/reg-event-fx
 ::fetch-todo-by-id
 (fn [{:keys [db]} [_ todo-id]]
   {:db (assoc db :selected-todo nil)
    :http-xhrio (assoc request-defaults
                       :method :get
                       :uri (str config/API_URL "/todos/" todo-id)
                       :on-success [::set-selected-todo])}))

(re-frame/reg-event-fx
 ::create-todo
 (fn [_ [_ args
         ;;{:keys [values]}
         ]]
   {:http-xhrio (assoc request-defaults
                       :method :post
                       :uri (str config/API_URL "/new")
                       :params args

                       :format (ajax/json-request-format)
                       :on-success [::navigate :todo-app.views/list])}))

(re-frame/reg-event-fx
 ::update-todo-by-id
 (fn [_ [_ todo-id args]]
   {:http-xhrio (assoc request-defaults
                       :method :post
                       :uri (str config/API_URL "/todos/" todo-id "/update")
                       :params args
                       :format (ajax/json-request-format)
                       :on-success [::navigate :todo-app.views/list])}))

(re-frame/reg-event-fx
 ::delete-todo-by-id
 (fn [_ [_ todo-id]]
   ;;[_ [_ todo-id args]]
   {:http-xhrio (assoc request-defaults
                       :method :post
                       :uri (str config/API_URL "/todos/" todo-id "/delete")
                       :format (ajax/json-request-format)
                       :on-success [::fetch-todos]
                       )}))
