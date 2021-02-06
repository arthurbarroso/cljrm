(ns cljrm.user.handler
  (:require [cljrm.user.db :as db]
            [ring.util.response :as rr]
            [buddy.hashers :refer [encrypt check]]
            [cljrm.responses :refer [base-url]]))

(defn find-users [db]
  (fn [request]
    (rr/response (db/find-all-users db))))

(defn find-single-user [db]
  (fn [request]
    (rr/response (db/find-single-user db (-> request :parameters :path :user-id)))))

(defn create-user! [db]
  (fn [request]
    (rr/created
      (str base-url "/user/"
           (:id
             (db/create-user!
               db
               (assoc
                 (-> request :parameters :body)
                 :password
                 (encrypt (-> request :parameters :body :password))
                 :created_at
                 (java.time.LocalDate/now))))))))

(defn login [db]
  (fn [request]
    (rr/response (db/login db
                           (-> request :parameters :body :email)
                           (-> request :parameters :body :password)))))