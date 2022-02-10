(ns todo-api.handler.routing
  (:require

   [integrant.core :as ig]
   [compojure.core :refer [GET POST routes]]

   [clojure.data.json :as json]
   [ring.util.response :as resp]))

(def todos
  (atom {1 {:id 1
            :task "build an API"}
         2 {:id 2
            :task "?????"}
         3 {:id 3
            :task "profit!"}
         4 {:id 4
            :task "gima326!"}}))

(defmethod ig/init-key :todo-api.handler/routing [_ duct-db]
  (let [spec (get-in duct-db [:db :spec])]
    (routes
     (GET  "/todos" []
           (-> @todos
               (vals)
               (json/write-str)
               (resp/response)
               (resp/content-type "application/json; charset=UTF-8"))))))
