(ns lights-out.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init
   (fn []
     (log/info "\n-=[lights-out started successfully]=-"))
   :stop
   (fn []
     (log/info "\n-=[lights-out has shut down successfully]=-"))
   :middleware identity})
