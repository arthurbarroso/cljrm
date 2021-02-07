(ns cljrm.user.utils
  (:require [buddy.sign.jwt :as jwt]))

(def jwt-secret "string")

(defn create-token [payload]
  {:token (jwt/sign payload jwt-secret)})
