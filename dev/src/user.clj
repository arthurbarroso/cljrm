(ns user
  (:require [integrant.repl :as ig-repl]
            [integrant.core :as ig]
            [integrant.repl.state :as state]
            [cljrm.server]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]))

(ig-repl/set-prep!
  (fn [] (-> "resources/config.edn" slurp ig/read-string)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :cljrm/app))
(def db (-> state/system :db/postgres))

(defn query-single [sql]
  (jdbc/execute-one! db sql {:return-keys true
                         :builder-fn rs/as-unqualified-maps}))

(comment
  (go)
  (app {:request-method :get
        :uri "/swagger.json"})
  (app {:request-method :get
        :uri "/v1/users"})
  (app {:request-method :post
        :uri "/v1/users"})
  (halt)
  (jdbc/execute! db ["SELECT * FROM users"])
  (dissoc  (jdbc/execute! db ["SELECT * FROM users"]) :password)
  (jdbc/execute! db ["SELECT * FROM users"]
                 {:result-set-fn (fn [rs]
                                   (map (fn [item]
                                          (println ("x")))) rs)})
  (let [x(jdbc/execute! db ["SELECT * FROM users"]
                           {:result-set-fn (fn [rs]
                                             (map (fn [i] (dissoc i :password)) rs))})]
    x)

  (jdbc/execute! db ["SELECT * FROM users"] {:result-set-fn (fn [rs] (println "xxx"))})
  (sql/query db :user)
  (->> (jdbc/execute! db ["SELECT * FROM users"] {:return-keys true
                                                 :builder-fn rs/as-unqualified-maps})
      (map (fn [user]
             (dissoc user :password :created_at))))
  (->> (jdbc/execute! db ["SELECT * FROM users"])
       (map (fn [user]
              (dissoc user :password :created_at)))
       (println))
  (->> (jdbc/execute! db ["SELECT * FROM users"] {:builder-fn rs/as-unqualified-maps})
       (map (fn [user]
              (dissoc user :password))))
  (reset))