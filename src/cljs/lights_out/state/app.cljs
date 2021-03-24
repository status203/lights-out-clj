(ns lights-out.state.app
  (:require [re-frame.core :as rf]

            [lights-out.state.routing]
            [lights-out.state.setup]
            [lights-out.state.game]))

;; TODO? Take a list of namespaces (or query lights-out.state?) and merge initial-state if there is one in ns.

(rf/reg-event-db
 :app/initial-state
 (fn [db [_ _]]
   (if (:initialised db)
     db
     (merge db
            lights-out.state.setup/initial-state
            lights-out.state.game/initial-state))))