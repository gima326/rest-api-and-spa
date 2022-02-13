(ns todo-app.views
  (:require [re-frame.core :as re-frame]
            [todo-app.subs :as subs]
            [todo-app.events :as events]

            ;; add
            [fork.core :as fork]))

;; 共通的な関数 ====================

(defn todo-form [props]
  [fork/form
   (merge {:prevent-default? true
           :clean-on-unmount? true}
          props)

   (fn [{:keys [values
                form-id
                handle-change
                handle-blur
                submitting?
                handle-submit]}]

     [:form {:id form-id
             :on-submit handle-submit}

      ;;
      [:label "Task"]
      [:input {:name "task"
               :value (values "task")
               :on-change handle-change
               :on-blur handle-blur}]

      ;;
      [:button {:type "submit"
                :disabled submitting?}
       "Submit"]])])

;; ================================

(defmulti view :handler)

(defmethod view :default [_] [:div "404 Not Found"])

(defmethod view ::home [_]
  [:div "Home"
   [:ul
    [:li [:button {:on-click #(re-frame/dispatch [::events/navigate ::list])}
          "View"]]
    [:li [:button {:on-click #(re-frame/dispatch [::events/navigate ::create])}
          "Create"]]]])

(defmethod view ::list [_]
  (re-frame/dispatch [::events/fetch-todos])
  [:div "Todo List"
   [:ul
    (map
     (fn [{:keys [id task]}]

       ;; changed
       ;; [:li {:key id} task]
       [:li {:key id} task
        [:button {:on-click #(re-frame/dispatch [::events/navigate ::edit {:id id}])}
         "Edit"]
        [:button {:on-click #(re-frame/dispatch [::events/delete-todo-by-id id])}
         "Delete"]
        ]
       )
     @(re-frame/subscribe [::subs/todos])
     )]
   ]
  )

(defmethod view ::create [_]
  [:div
   [:p "Create New Todo"]
   [todo-form {
               ;;:initial-values {"task" ""}
               :on-submit #(re-frame/dispatch [::events/create-todo %])}]])

;; 「route-params」は予約語

(defmethod view ::edit [{:keys [route-params]}]
  [:div (str "Edit Todo " (:id route-params))
   (when-let [{:keys [id task]} (first @(re-frame/subscribe [::subs/selected-todo]))]

     [todo-form {:initial-values {"task" task}
                 :on-submit #(re-frame/dispatch [::events/update-todo-by-id id %])
                 }
      ])])

(defn main-panel []
  [:div
   [:div
    {:on-click #(re-frame/dispatch [::events/navigate ::home])}
    "Todo App"]
   [view @(re-frame/subscribe [::subs/current-route])]])
