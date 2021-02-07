(ns cljrm.test-utils
  (:require [clojure.test :refer :all]
            [integrant.repl.state :as state]
            [ring.mock.request :as mock]
            [muuntaja.core :as m]))

(def token (atom nil))

(defn get-test-token []
  "token")

(defn test-endpoint
  ([method uri]
   (test-endpoint method uri nil))
  ([method uri opts]
   (let [app (-> state/system :cljrm/app)
         request (app (-> (mock/request method uri)
                          (cond-> (:auth opts) (mock/header :authorization (str "Bearer " (or @token (get-test-token))))
                                  (:body opts) (mock/json-body (:body opts)))))]
     (update request :body (partial m/decode "application/json")))))

(comment
  (test-endpoint :get "/v1/user/1" {}))