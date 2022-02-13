(ns todo-api.boundary.todos
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.core :refer [format]]))

(defprotocol Todos
  (get-todos [db])
  (get-todo [db id])
  (create-todo [db params])
  (update-todo [db id params])
  (delete-todo [db id]))

(extend-protocol Todos
  clojure.lang.PersistentArrayMap

  (get-todos [db]
   (jdbc/query db ["SELECT * FROM todos;"]))

  (get-todo [db id]
   (jdbc/query db [(format "SELECT * FROM todos WHERE id = '%s';" id)]))

  (create-todo [db params]
   (jdbc/insert! db :todos params))

  (update-todo [db id params]
   (jdbc/update! db :todos params ["id=?" id]))

  (delete-todo [db id]
   (jdbc/delete! db :todos ["id=?" id])))
