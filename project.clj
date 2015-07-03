(defproject govhack "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
			:url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[compojure "1.3.4"]
				 [org.clojure/clojure "1.6.0"]
				 [org.clojure/data.csv "0.1.2"]
				 [mysql/mysql-connector-java "5.1.32"]
				 [yesql "0.4.0"]]
  :plugins [[lein-ring "0.9.6"]]
  :ring {:handler govhack.core/app})
