(ns lights-out.state.display
  (:require 
   [re-frame.core :as rf]

   [lights-out.domain :as domain]
   [lights-out.state.core :as los]))

(los/reg-event-db
 :display/game
 (fn [db [_ board]]
   (assoc-in db [::los/display] {::los/display-type :game
                                 ::los/board board})))


;; Subscriptions

(rf/reg-sub
 :display/board
 (fn [{{{:keys [::los/grid ::los/grid-size ::los/move] :as board} ::los/board} ::los/display} [_]]
   (when board
     (let [completed? (domain/completed? grid)]
       {:grid-size grid-size
        :grid grid
        :move move
        :completed? completed?
        :move-label (if completed? "" (domain/cell->label move grid-size))}))))
