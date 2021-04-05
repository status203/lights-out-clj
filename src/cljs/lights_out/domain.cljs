(ns lights-out.domain)

(defn new-board
  "Returns a vector of true/false indicating which cells are lit. Cells run
  left->right, top->bottom"
  [size]
  (into [] (repeatedly (* size size) #(pos? (rand-int 2)))))

(defn toggle-cell
  "Given a board and the index of a cell, returns a new board with the specified
   cell (and it's cardinal neighbours lit status toggled)"
  [board cell]
  board)