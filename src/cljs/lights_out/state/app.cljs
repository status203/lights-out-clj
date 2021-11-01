(ns lights-out.state.app
  (:require [re-frame.core :as rf]
            [lights-out.state.routing]

            [lights-out.state.core :as los]
            [lights-out.state.display]
            [lights-out.state.game]
            [lights-out.state.setup]
            [lights-out.state.history]
            [lights-out.state.options]))

(rf/reg-event-db
 :app/initial-state
 (fn [db [_ _]]
   (merge db los/initial-state)))