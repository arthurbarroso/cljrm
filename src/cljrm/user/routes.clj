(ns cljrm.user.routes
  (:require [cljrm.user.handler :as handler]
            [cljrm.middleware :refer [wrap-jwt-authentication auth-middleware]]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    [["/users"
      {:get {:middleware [wrap-jwt-authentication
                          auth-middleware]
             :handler (handler/find-users db)}}]
     ["/user/:user-id"
      {:get {:handler (handler/find-single-user db)
             :parameters {:path {:user-id integer?}}}}]
     ["/user"
      {:post {:handler (handler/create-user! db)
              :parameters {:body {:email string?
                                  :password string?}}}}]
     ["/login"
      {:post {:handler (handler/login db)
              :parameters {:body {:email string?
                                  :password string?}}}}]]))