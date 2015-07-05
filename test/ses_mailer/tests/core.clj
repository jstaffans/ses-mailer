(ns ses-mailer.tests.core
  (:require [expectations :refer :all]
            [ses-mailer.core :as ses]))

(expect
  ["a"]
  (.getToAddresses (ses/create-destination "a")))

(expect
  ["a" "b"]
  (.getToAddresses (ses/create-destination ["a" "b"])))


