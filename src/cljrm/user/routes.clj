(ns cljrm.user.routes
  (:require [cljrm.user.handler :as handler]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    [["/users"
      {:get {:handler (handler/find-users db)}
       :post {:handler (fn [request] {:ok true})}}]
     ["/user/:user-id"
      {:get {:handler (handler/find-single-user db)
             :parameters {:path {:user-id integer?}}}}]
     ["/user"
      {:post {:handler (handler/create-user! db)
              :parameters {:body {:email string?
                                  :password string?}}}}]]))