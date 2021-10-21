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

(los/reg-event-db
 :display/historical
 (fn [{display ::los/display :as db} [_ board]]
   (-> db
       (assoc-in [::los/display] {::los/display-type :history
                                  ::los/board board})
       (assoc-in [::los/stored-display] display))))

(los/reg-event-db
 :display/stored
 (fn [{display ::los/stored-display :as db} [_]]
   (assoc db ::los/display display)))


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

(rf/reg-sub
 :display/historical?
 (fn [{{type ::los/display-type} ::los/display} [_]]
   (= :history type)))
