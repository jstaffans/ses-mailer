(ns ses-mailer.core
  (:require [taoensso.encore :refer [doto-cond]])
  (:import (com.amazonaws.services.simpleemail AmazonSimpleEmailServiceClient)
           (com.amazonaws.services.simpleemail.model SendEmailRequest Content Message Body Destination)
           (com.amazonaws.auth BasicAWSCredentials DefaultAWSCredentialsProviderChain AWSCredentialsProvider AWSCredentials)))

(defn make-client
  ([] (AmazonSimpleEmailServiceClient.))
  ([config] (AmazonSimpleEmailServiceClient. config)))

(def ses-client*
  (memoize
    (fn [{:keys [provider access-key secret-key] :as client-opts}]
      (if (empty? client-opts)
        (make-client)
        (let [^AWSCredentials creds
              (when-not provider (BasicAWSCredentials. access-key secret-key))
              ^AWSCredentialsProvider provider
              (or provider (when-not creds (DefaultAWSCredentialsProviderChain.)))]
          (make-client (or provider creds)))))))

(defn ses-client ^AmazonSimpleEmailServiceClient [client-opts] (ses-client* client-opts))

(defn create-destination
  [to]
  (let [to-addresses (if (vector? to) to (vector to))]
    (.withToAddresses (Destination.) to-addresses)))

(defn create-body
  [html-body text-body]
  (if (or html-body text-body)
    (doto-cond [b (Body.)]
      html-body (.withHtml (Content. html-body))
      text-body (.withText (Content. text-body)))
    (Body. (Content. ""))))

(defn send-email-request
  [from to subject & [{:keys [html-body text-body]}]]
  (let [destination (create-destination to)
        message (-> (Message. (Content. subject) (create-body html-body text-body)))]
    (SendEmailRequest. from destination message)))

(defn send-email
  "Send an email to one or multiple recipients. Body is optional.

  (send-email client-opts
    \"no-reply@company.com\"
    \"lucky-customer@me.com\"
    \"You are our millionth customer!\"
    {:html-body \"<html>Awesome html content</html>
     :text-body \"Boring text content\"}
  "
  [client-opts from to subject & [opts]]
  (.sendEmail
    (ses-client client-opts)
    (send-email-request from to subject opts)))
