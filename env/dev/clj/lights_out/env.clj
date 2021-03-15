(ns lights-out.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [lights-out.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[lights-out started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[lights-out has shut down successfully]=-"))
   :middleware wrap-dev})
