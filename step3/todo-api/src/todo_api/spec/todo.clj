(ns todo-api.spec.todo
  (:require [clojure.spec.alpha :as s]
            [phrase.alpha :refer [phrase defphraser phrase-first]]))

(def email-regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")
(defn email-format? [email] (re-matches email-regex email))

(s/def ::email
  (s/and not-empty string? email-format?))

(defphraser not-empty
  {:via [::email]}
  [_ _]
  "Please input email.")

(defphraser string?
  {:via [::email]}
  [_ _]
  "Please input email as string.")

(defphraser email-format?
  {:via [::email]}
  [_ _ _]
  "Please input as email format.")

;;==============

(s/def ::age not-empty)

(defphraser not-empty
  {:via [::age]}
  [_ _]
  "Please input age.")

;;==============

(s/def ::name not-empty)

(defphraser not-empty
  {:via [::name]}
  [_ _]
  "Please input name.")

;;==============

(s/def ::todo (s/keys :req [::name ::age ::email]))

(defn todo->spec-todo [todo]
  {::name (:name todo)
   ::age (:age todo)
   ::email (:email todo)})

(defn spec-todo->todo [spec-todo]
  {:name (::name spec-todo)
   :age (::age spec-todo)
   :email (::email spec-todo)})

(defn valid? [todo]
  (s/valid? ::todo (todo->spec-todo todo)))

(defn error-message [todo]
  (phrase-first {} ::todo (todo->spec-todo todo)))

(defn error-messages [todo]
  (->>
   (for [problem (some->> (todo->spec-todo todo)
                          (s/explain-data ::todo)
                          (::s/problems))]
     (let [message (phrase {} problem)
           path (first (:path problem))]
       [path message]))
   (into {})
   spec-todo->todo))
