(ns govhack.core
  (:require [clojure.data.json :as json]
			[clojure.string :refer [join]]
			[compojure.core :refer [defroutes GET POST]]
			[compojure.route :refer [resources not-found]]
			[compojure.handler :refer [site]]
			[yesql.core :refer [defqueries]]
			[net.cgrand.enlive-html :as html]
			))


(defqueries "sql/queries.sql")


(def DB {:subprotocol "mysql"
		 :subname "//localhost:3306/govhack"
		 :user "root"
		 :password "vmengrtbyj"})


(defn jsonify
  [coll]
  (->> coll (map json/write-str) (join ",") (apply str)))


(html/deftemplate map-template "html/_layout.html"
  []
  [:#placeList]
  (html/content
   (str "var places=[" (jsonify (find-places DB)) "]")))


(html/defsnippet
  place-snippet
  "html/place.html"
  [:#place]
  [place]
  [:h2] (html/content (:title place))
  [:#place-id] (html/set-attr :value (:id place)))


(html/defsnippet
  memory-snippet
  "html/memory.html"
  [:.memory]
  [memory]
  [:.details] (html/content (:details memory))
  [:.created] (html/content (str "Created: " (:created memory)))
  )


(html/deftemplate place-template "html/_layout.html"
  [id]
  [:#content] (html/content (place-snippet (first (find-place-by-id DB id))))
  [:#placeList] (html/content (str "var places=[" (jsonify (find-place-by-id DB id)) "]"))
  [:#memories :div :div] (html/content (memory-snippet (first (find-memories-by-place-id DB id))))
  )


(defroutes routes
  (GET "/" [] (map-template))
  (GET "/map" [] (map-template))
  (GET "/place/:id{[0-9]+}" [id] (place-template id))
  (POST "/memory/add" [memory-text place-id] (do (insert-memory! DB place-id memory-text) "DONE"))
  (resources "/")
  (not-found "Page not found"))


(def app (site routes))

