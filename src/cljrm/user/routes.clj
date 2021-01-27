(ns cljrm.user.routes
  (:require [cljrm.user.handler :as handler]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    ["/users"
     {:get {:handler (handler/find-users db)}
      :post {:handler (fn [request] {:ok true})}}]))