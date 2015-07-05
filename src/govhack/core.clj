(ns govhack.core
  (:require [clojure.data.json :as json]
			[clojure.string :refer [join]]
			[compojure.core :refer [defroutes GET POST]]
			[compojure.route :refer [resources not-found]]
			[compojure.handler :refer [site]]
			[ring.util.response :refer [redirect]]
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


(defn to-vals
  "Return map with f applied to values"
  [f coll]
  (zipmap (keys coll) (map f (vals coll))))

(defn to-keys
  "Return map with f applied to keys"
  [f coll]
  (zipmap (map f (keys coll)) (vals coll)))


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
  [:.timestamp] (html/content (str (:created memory))))


(html/defsnippet
  response-snippet
  "html/response.html"
  [:.response]
  [response]
  [:td.title] (html/content (:title response))
  [:td.value] (html/content (str (:value response)))
  [:td.count] (html/content (str (:response_count response)))
  )


(html/defsnippet
  photo-snippet
  "html/photo.html"
  [:.photo]
  [photo]
  [:img] (html/set-attr :src (:url photo))
  [:a] (html/set-attr :href (:url photo))
  [:.timestamp] (html/content (str (:created photo))))


(html/defsnippet
  poll-snippet
  "html/poll.html"
  [:.poll]
  [poll]
  [:.title] (html/content (:title poll))
  [(html/attr= :type "range")] (html/set-attr :name (str "poll-response-" (:id poll))))


(html/deftemplate place-template "html/_layout.html"
  [id]
  [:#content] (html/content (place-snippet (first (find-place-by-id DB id))))
  [:#placeList] (html/content (str "var places=[" (jsonify (find-place-by-id DB id)) "]"))
  [:#memories :div :div] (html/content (map memory-snippet (find-memories-by-place-id DB id)))
  [:#artefacts :div :div] (html/content (map photo-snippet (find-photos-by-place-id DB id)))
  [:#value :div :div] (html/content (map poll-snippet (find-polls DB)))
  [:#responses] (html/content (map response-snippet (find-aggregated-responses-by-place-id DB id)))
  [(html/attr= :type "hidden")] (html/set-attr :value id)
  )


(defn poll-response-handler
  [params]
  (let [params2 (to-vals #(Integer/parseInt %) (into {} params))
		place-id (:place-id params2)
		extract-poll-id #(Integer/parseInt (last (re-find #"poll-response-(\d+)" (name %))))
		responses (to-keys extract-poll-id (dissoc params2 :place-id))]
	(doseq [[poll-id value] responses]
	  (insert-response! DB poll-id value place-id))
	(redirect (str "/place/" place-id))
	))


(defroutes routes
  (GET "/" [] (map-template))
  (GET "/map" [] (map-template))
  (GET "/place/:id{[0-9]+}" [id] (place-template id))
  (POST "/memory/add"
		[memory-text place-id]
		(do
		  (insert-memory! DB place-id memory-text)
		  (redirect (str "/place/" place-id))
		  ))
  (POST "/photo/add"
		[photo-url place-id]
		(do
		  (insert-photo! DB place-id photo-url)
		  (redirect (str "/place/" place-id))
		  ))
  (POST "/response/add" [& params] (poll-response-handler params))
  (resources "/")
  (not-found "Page not found"))


(def app (site routes))

