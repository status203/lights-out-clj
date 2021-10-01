(ns lights-out.state.app
  (:require [re-frame.core :as rf]
            [lights-out.state.core :as los]
            [lights-out.state.routing]
            [lights-out.state.setup]
            [lights-out.state.game]))

(rf/reg-event-db
 :app/initial-state
 (fn [db [_ _]]
   (merge db los/initial-state)))