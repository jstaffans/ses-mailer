(ns ses-mailer.tests.core
  (:require [expectations :refer :all]
            [ses-mailer.core :as ses]))

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
