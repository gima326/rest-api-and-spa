(ns todo-api.view.todo
  (:use [hiccup.core]))

(defn show-simple-view [msg]
  (html [:span msg]))

(defn error-message-line [err-msg]
  [:div {:style "background: #fcc; margin-bottom: 5px;"} err-msg])

;;============================================================

;; 関数版のほうが、マクロ版よりクォートなどがちょっとだけ少ない。

(defn foo-fn [u param_name err-msgs]
 (let [field# (keyword param_name)]
  `[:div
    [:label {:for ~param_name} ~param_name]
    [:input {:name ~param_name :value ~(field# u)}]
    ~(error-message-line (field# err-msgs))]))

(defmacro foo-mac [u param_name err-msgs]
 (let [field# (keyword param_name)]
  `[:div
    [:label {:for ~param_name} ~param_name]
    [:input {:name ~param_name :value (~field# ~u)}]
    ~(error-message-line `(~field# ~err-msgs))]))

;;============================================================

(defn todo-form [action todo & {:keys [error-messages]}]
  [:form {:action action :method "post"}

;;   (foo-mac todo "name" error-messages)
;;   (foo-mac todo "email" error-messages)
;;   (foo-mac todo "age" error-messages)

;; マクロを for のなか使うと、意図しない挙動になるみたい。
;;   (for [field ["name" "email" "age"]]
;;     (foo-mac todo field error-messages))

   (for [field ["name" "email" "age"]]
     (foo-fn todo field error-messages))

   [:div
    [:a {:href "/todos"} "show"]]

   [:button {:type "submit"} "Submit"]])

(defn edit-todo-view [todo-id todo error-messages]
  (html [:div "[ Edit todo ]"
         (todo-form (str "/todos/" todo-id "/update") todo
                    :error-messages error-messages)]))

(defn new-todo-view [todo error-messages]
  (html [:div "[ New todo ]"
         (todo-form "/new" todo
                    :error-messages error-messages)]))

(defn show-todos-view [todos]
  (html [:div
         [:div "[ todos ]"]
         [:a {:href "/new"} "Add todo"]
         [:table
          [:thead
           [:tr
            [:th "id"]
            [:th "name"]]]
          [:tbody
           (for [todo todos]
             [:tr
              [:td [:a {:href (str "/todos/" (:id todo))} (:id todo)]]
              [:td (:name todo)]])]]]))

(defn show-todo-view [todo]
  (html [:div "[ todo ]"
         [:div
          (pr-str todo)
         [:div
          [:a {:href "/todos"} "show"]]
         [:div
          [:a {:href (str "/todos/" (:id todo) "/edit")} "edit"]
          [:form {:action (str "/todos/" (:id todo) "/delete") :method "post"}
           [:button {:type "submit"} "delete"]]]]]))