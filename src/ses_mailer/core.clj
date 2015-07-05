(ns ses-mailer.core
  (:require [taoensso.encore :refer [doto-cond]])
  (:import (com.amazonaws.services.simpleemail AmazonSimpleEmailServiceClient)
           (com.amazonaws.services.simpleemail.model SendEmailRequest Content Message Body Destination)
           (com.amazonaws.auth BasicAWSCredentials DefaultAWSCredentialsProviderChain AWSCredentialsProvider AWSCredentials)))

(def ses-client*
  (memoize
    (fn [{:keys [provider access-key secret-key] :as client-opts}]
      (if (empty? client-opts)
        (AmazonSimpleEmailServiceClient.)
        (let [^AWSCredentials creds
              (when-not provider (BasicAWSCredentials. access-key secret-key))
              ^AWSCredentialsProvider provider
              (or provider (when-not creds (DefaultAWSCredentialsProviderChain.)))]
          (AmazonSimpleEmailServiceClient. (or provider creds)))))))

(defn ses-client ^AmazonSimpleEmailServiceClient [client-opts] (ses-client* client-opts))

(defn create-destination
  [to]
  (let [to-addresses (if (vector? to) to (vector to))]
    (.withToAddresses (Destination.) to-addresses)))

(defn create-body
  [html_body text_body]
  (doto-cond [b (Body.)]
    html_body (.withHtml (Content. html_body))
    text_body (.withText (Content. text_body))))

(defn send-email-request
  [from to subject & [{:keys [html_body text_body]}]]
  (let [destination (create-destination to)
        message (-> (Message. (Content. subject) (create-body html_body text_body)))]
    (-> (SendEmailRequest.) (.withSource from) (.withDestination destination) (.withMessage message))))

(defn send-email
  "Send an email to one or multiple recipients. Body is optional.

  (send-email client-opts
    \"no-reply@company.com\"
    \"lucky-customer@me.com\"
    \"You are our millionth customer!\"
    {:html_body \"<html>Awesome html content</html>
     :text_body \"Boring text content\"}
  "
  [client-opts from to subject & [opts]]
  (println opts)
  (.sendEmail
    (ses-client client-opts)
    (send-email-request from to subject opts)))
