(defproject ses-mailer "0.0.3"
  :author "Johannes Staffans"
  :description "Clojure SES client"
  :url "https://github.com/jstaffans/ses-mailer"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [joda-time "2.8"]
                 [com.amazonaws/aws-java-sdk-ses "1.9.39" :exclusions [joda-time]]
                 [com.taoensso/encore "1.34.0"]]
  :profiles {:dev {:dependencies [[im.chit/vinyasa "0.3.4"]]}
             :test {:dependencies [[expectations "2.1.2"]]
                    :plugins      [[lein-expectations "0.0.8"]
                                   [lein-autoexpect "1.6.0"]]}})
