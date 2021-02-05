(ns user
  (:require [integrant.repl :as ig-repl]
            [integrant.core :as ig]
            [integrant.repl.state :as state]
            [cljrm.server]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [buddy.hashers :refer [encrypt]]))

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
  (sql/find-by-keys db :users {:id 3})
  (->> (sql/find-by-keys db :users {:id 3} {:return-keys true
                                            :builder-fn rs/as-unqualified-maps})
       (map (fn [user]
              (dissoc user :password :created_at))))
  (app {:request-method :get
        :uri "/v1/user/3"})

  (let [user {:email "a"
              :password "b"}
        encrypted-pass "c"]
    (assoc user :password encrypted-pass))
  (let [request {:body {:email "abdcefgh" :password "b"}}
        base-url "cic"]
   (str base-url "/user/" (:users/id  (sql/insert! db :users
                                          (assoc (-> request :body) :password
                                                                    (encrypt (-> request :body :password))
                                                                    :created_at (java.time.LocalDate/now))))))
  (java.time.LocalDate/now)
  (app {:request-method :post
        :uri "/v1/user"
        :body-params {:email "arthurzinzikabala@hotmail.com"
                      :password "1234supersafe"}})
  (reset))