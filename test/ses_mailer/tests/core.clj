(ns ses-mailer.tests.core
  (:require [expectations :refer :all]
            [ses-mailer.core :as ses])
  (:import (com.amazonaws.regions Regions)
           (com.amazonaws.auth DefaultAWSCredentialsProviderChain)
           (com.amazonaws AmazonWebServiceClient)))

(defn field
  [class field-name obj]
  (-> class (.getDeclaredField (name field-name))
    (doto (.setAccessible true))
    (.get obj)))

;; Client configuration

(expect
  Regions/EU_WEST_1
  (ses/aws-region :eu-west-1))

(expect
  #"eu-west-1"
  (->> (ses/ses-client {:region :eu-west-1 :provider (DefaultAWSCredentialsProviderChain.)})
       (field AmazonWebServiceClient "endpoint")
       .toString))

;; E-mail contents

(expect
  ["a"]
  (.getToAddresses (ses/create-destination "a")))

(expect
  ["a" "b"]
  (.getToAddresses (ses/create-destination ["a" "b"])))

(expect
  ""
  (-> (ses/create-body nil nil)
    (.getText)
    (.getData)))

(expect
  "<html></html>"
  (-> (ses/create-body "<html></html>" nil)
    (.getHtml)
    (.getData)))

(expect
  "Text"
  (-> (ses/create-body "<html></html>" "Text")
    (.getText)
    (.getData)))


