(ns todo-app.routes
  (:require [accountant.core :as accountant]
            [bidi.bidi :as bidi]))

(def routes
  ["/" {
;;        ""       :todo-app.views/home
;;        "list"   :todo-app.views/list
        ""   :todo-app.views/list

        "create" :todo-app.views/create
        ["edit/" [ #"\d+" :id ]] :todo-app.views/edit
        }

   ])

;; changed
;; (defn navigate [view]
;;   (accountant/navigate! (bidi/path-for routes view)))

(def path-for (partial bidi/path-for routes))

(defn navigate
  ([view] (navigate view {}))
  ([view params]
   ;;(accountant/navigate! (apply bidi/path-for routes view (apply concat params)))
   (accountant/navigate! (apply path-for view (apply concat params)))
   ))
