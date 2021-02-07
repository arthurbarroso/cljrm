(ns user
  (:require [integrant.repl :as ig-repl]
            [integrant.core :as ig]
            [integrant.repl.state :as state]
            [cljrm.server]
            [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [next.jdbc.result-set :as rs]
            [buddy.hashers :refer [encrypt check]]
            [buddy.auth.backends :as backends]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [buddy.sign.jwt :as jwt]))

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

(def jwt-secret "string")
(def backend (backends/jws {:secret jwt-secret}))

(defn wrap-jwt-authentication [handler]
  (wrap-authentication handler backend))

(defn create-token [payload]
  (jwt/sign payload jwt-secret))

(comment
  (go)
  (app {:request-method :get
        :uri "/swagger.json"})
  (app {:request-method :get
        :uri "/v1/users"
        :headers {:authorization (str "Token " (create-token {:name "arthur"}))}})
  (app {:request-method :get
        :uri "/v1/users"})
  (app {:request-method :post
        :uri "/v1/users"})
  (app {:request-method :get
        :uri "/v1/user/1"})
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
        :body-params {:email "arthurzinzikabal3a@hotmail.com"
                      :password "1234supersafe"}})

  (check "arthur1" (:users/password (jdbc/execute-one! db ["SELECT * FROM users where id = 1"])))
  (jdbc/execute! db ["SELECT * FROM users"])
  (check "1234supersafe" (:password (jdbc/execute-one! db ["SELECT * FROM users where email ilike"] {:builder-fn rs/as-unqualified-maps})))
  (let [password "1234supersafe"]
    (check password (:password
                      (first (sql/find-by-keys
                               db :users {:email "arthurzinzika@hotmail.com"}
                               {:builder-fn rs/as-unqualified-maps})))))

  (app {:request-method :post
        :uri "/v1/login"
        :body-params {:email "arthurzinzikabala@hotmail.com"
                      :password "1234supersafe"}})

  (create-token {:name "arthur"})
  (jwt/unsign (create-token {:name "arthur"}) jwt-secret)
  (jwt/unsign "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImFydGh1cnppbnppa2FiYWxhQGhvdG1haWwuY29tIn0.toA7MVayuOvUZoGuEo6g5RsF6wpjFD3IPeJVzBbm1t4" jwt-secret)

  (str "Token " (create-token {:name "arthur"}))
  (create-token {:name "arthur"})
  (reset))
