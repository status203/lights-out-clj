(ns lights-out.routes.home
  (:require
   [lights-out.layout :as layout]
   [lights-out.middleware :as middleware]
   [ring.util.response]))

(defn home-page [request]
  (layout/render request "home.html"))

(defn home-routes []
  [""
   {:middleware [middleware/wrap-csrf
                 middleware/wrap-formats]}
   ["/" {:get home-page}]])

