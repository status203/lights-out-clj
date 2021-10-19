(ns lights-out.state.game
  (:require
   [re-frame.core :as rf]
   
   [lights-out.domain :as domain]
   [lights-out.state.core :as los]))

(los/reg-event-fx
 :game/new-game
 (fn [{:keys [db]} [_ size]]
   (let [{{:keys [::los/grid]} ::los/board :as game} (domain/new-game size)]
     {:db (assoc db ::los/game game)
      :dispatch [:display/game grid size]})))

(los/reg-event-fx
 :game/toggle-cell
 (fn [{db :db} [_ cell]]
   (let [old-game (::los/game db)
         {{:keys [::los/grid-size ::los/grid]} ::los/board :as game} (domain/toggle-cell-in-game old-game cell)]
     {:db (assoc-in db [::los/game] game)
      :dispatch [:display/game grid grid-size]})))

(los/reg-event-db
 :game/enter-cell
 (fn [db [_ cell-label]]
   (assoc-in db [::los/game ::los/board ::los/hovered-cell] cell-label)))

(los/reg-event-db
 :game/leave-cell
 (fn [db [_]]
   (assoc-in db [::los/game ::los/board ::los/hovered-cell] nil)))

;; subscriptions

(rf/reg-sub
 :game/game
 (fn [db [_]]
   (-> db ::los/game)))

(rf/reg-sub
 :game/board
 (fn [db _]
   (-> db ::los/game ::los/board)))

(rf/reg-sub
 :game/hovered-cell
 :<- [:game/board]
 (fn [board [_]]
   (when board (::los/hovered-cell board))))

(rf/reg-sub
 :game/grid
 :<- [:game/board]
 (fn [board [_]]
   (when board (::los/grid board))))

(rf/reg-sub
 :game/grid-size
 :<- [:game/board]
 (fn [board [_]]
   (when board (::los/grid-size board))))

(rf/reg-sub
 :game/history
 :<- [:game/game]
 (fn [game [_]]
   (when game (::los/history game))))

(rf/reg-sub
 :game/move-label
 (fn [db [_ move-index size]]
   (domain/cell->label (get-in db [::los/game ::los/history move-index ::los/move])
                       size)))

(rf/reg-sub
 :game/completed?
 :<- [:game/grid]
 (fn [grid _] (when grid (domain/completed? grid))))


