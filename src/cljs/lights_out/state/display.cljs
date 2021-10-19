(ns lights-out.state.display
  (:require 
   [re-frame.core :as rf]

   [lights-out.domain :as domain]
   [lights-out.state.core :as los]))

(los/reg-event-db
 :display/game
 (fn [db [_ grid size]]
   (assoc db ::los/display {::los/display-type :game
                            ::los/grid-size size
                            ::los/grid grid})))

;; Subscriptions

; Returns {:display-type ... :grid-size ... :grid .... :completed ...}
(rf/reg-sub
 :display/display
 (fn [{{:keys [::los/display-type
               ::los/grid-size
               ::los/grid]} ::los/display} [_]]
   {:display-type display-type
    :grid-size grid-size
    :grid grid
    :completed (domain/completed? grid)}))
