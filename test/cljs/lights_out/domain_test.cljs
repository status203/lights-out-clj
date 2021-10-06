(ns lights-out.domain-test
  (:require
   [cljs.test :refer-macros [deftest testing is]]
   [pjstadig.humane-test-output]

   [lights-out.domain :as sut]
   [lights-out.state.core :as los]))

(deftest succeeded?
  (is (= false (sut/succeeded? [false false true])))
  (is (= true (sut/succeeded? [false false false]))))

(deftest new-game
  (testing "grid-size copied in to game correctly"
    (is (= 1 (->> (sut/new-game 1) ::los/board ::los/grid-size))))
  (testing "grid is expected size"
    (is (= 9 (->> (sut/new-game 3) ::los/board ::los/grid count))))
  (testing "cells are all boolean"
    (is (->> (sut/new-game 4) ::los/board ::los/grid (every? boolean?)))))

(deftest neighbours
  (let [board {::los/grid-size 3 ::los/grid (into [] (repeat 9 false))}]
    (is (= [true  true  false
            true  false false
            false false false] 
           (::los/grid (sut/toggle-cell-in-board board 0))))
    (is (= [false true  false
            true  true  true
            false true  false]
           (::los/grid (sut/toggle-cell-in-board board 4))))
    (is (= [false false false
            false false true
            false true  true]
           (::los/grid (sut/toggle-cell-in-board board 8))))))