(ns lights-out.state.game
  (:require
   [re-frame.core :as rf]
   
   [lights-out.domain :as domain]
   [lights-out.state.core :as los]))

(los/reg-event-fx
 :game/new-game
 (fn [{:keys [db]} [_ size]]
   (let [board (domain/new-board size)]
     {:db (-> db 
              (assoc ::los/game board)
              (assoc ::los/history [board]))
      :dispatch [:display/game board]})))

(los/reg-event-fx
 :game/toggle-cell
 (fn [{{:keys [::los/game] :as db} :db} [_ cell]]
   (let [new-board (domain/toggle-cell-in-board game cell)]
     {:db (-> db
              (assoc-in [::los/game] new-board)
              (update-in [::los/history] #(conj % new-board)))
      :dispatch [:display/game new-board]})))

(los/reg-event-fx
 :game/enter-cell
 (fn [{{:keys [::los/game] :as db} :db} [_ cell]]
   (let [new-board (assoc game ::los/move cell)]
     {:db (assoc-in db [::los/game] new-board)
      :dispatch [:display/game new-board]})))

(los/reg-event-fx
 :game/leave-cell
 (fn [{{:keys [::los/game] :as db} :db} [_]]
   (let [new-board (assoc game ::los/move nil)]
     {:db (assoc-in db [::los/game] new-board)
      :dispatch [:display/game new-board]})))

;; subscriptions

(rf/reg-sub
 :game/game
 (fn [db [_]]
   (-> db ::los/game)))

(rf/reg-sub
 :game/move
 (fn [db [_]]
   (::los/move db)))

(rf/reg-sub
 :game/history
 (fn [db [_]]
   (::los/history db)))



