(ns cljrm.user.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.result-set :as rs]
            [next.jdbc.sql :as sql]
            [buddy.hashers :refer [encrypt check]]))

(defn find-all-users [db]
  (->> (jdbc/execute! db ["SELECT * FROM users"]
                      {:builder-fn rs/as-unqualified-maps})
       (map (fn [user]
              (dissoc user :password)))))

(defn find-single-user [db id]
  (->> (sql/find-by-keys db :users {:id id} {:return-keys true
                                            :builder-fn rs/as-unqualified-maps})
       (map (fn [user]
              (dissoc user :password :created_at)))))

(defn create-user! [db user]
  (sql/insert! db :users user {:builder-fn rs/as-unqualified-maps}))


(defn login [db email password]
  (check password (:password
                    (first (sql/find-by-keys
                             db :users {:email email}
                             {:builder-fn rs/as-unqualified-maps})))))