(ns todo-api.handler.routing
  (:require
   [integrant.core :as ig]
   [compojure.core :refer [GET POST routes]]
   [compojure.route :as route]

   [clojure.data.json :as json]
   [ring.util.response :as resp]

   [todo-api.boundary.todos :as db.todos]
   ))

;;============================================================

(defn gen-response [rslt]
  (-> rslt
      (json/write-str)
      (resp/response)
      (resp/content-type "application/json; charset=UTF-8")))

(defn get-content-from-request [request]
  (-> (:body request)
      (slurp)
      (json/read-str :key-fn keyword)))

(defn- do-something []
  (throw (ex-info "err" {:exception/type :server-error})))

(defn throw-exception-handler []
  (let [res (do-something)]
    (resp/response res)))

;;============================================================

(defmethod ig/init-key :todo-api.handler/routing [_ duct-db]
  (let [spec (get-in duct-db [:db :spec])]
    (routes
     (GET  "/todos"             []   (gen-response (db.todos/get-todos spec)))
     (GET  "/todos/:id"         [id] (gen-response (db.todos/get-todo spec id)))

     (POST "/new"               []   (fn [args]
                                       ;; 項目「values」より値を取得
                                       (let [content (:values (get-content-from-request args))]
                                        (if (db.todos/create-todo spec content)
                                          (gen-response (db.todos/get-todos spec))))))

     (POST  "/todos/:id/update" [id] (fn [args]
                                       ;; 項目「dirty」より値を取得
                                       (let [content (:dirty (get-content-from-request args))]
                                         (if (= '(1) (db.todos/update-todo spec id content))
                                           (gen-response (db.todos/get-todo spec id))))))

     (POST "/todos/:id/delete"  [id] (fn [_]
                                       (if (= '(1) (db.todos/delete-todo spec id))
                                         ;; 正常終了時には、何か値を返してやらないとエラーになる。
                                         '(1)
                                         (throw-exception-handler)))))))
