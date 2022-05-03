%====================================================================================
% storage description   
%====================================================================================



% POSITIONS

pos(home, 0, 0).
pos(bar, 6, 0).
pos(entrance, 0, 4).
pos(exit, 6, 4).
pos(table1, 2, 2).
pos(table2, 4, 2).



% TABLES

% tableOccupied(ID)
% tableDirty
% tableCleared
% tableCleaned
% tableSanitized

stateTable(1, tableSanitized).
stateTable(2, tableSanitized).

setTableState(TABLE, STATE) :-
	retract(stateTable(TABLE, _)),
	!,
	assert(stateTable(TABLE, STATE)).

occupyTable(TABLE, ID) :-
	retract(stateTable(TABLE, tableSanitized)),
	!,
	assert(stateTable(TABLE, tableOccupied(ID))).



% WAITER

stateWaiter(home).

setWaiterState(STATE) :-
	retract(stateWaiter(_)),
	!,
	assert(stateWaiter(STATE)).



% BARMAN

stateBarman(waitOrder).

setBarmanState(STATE) :-
	retract(stateBarman(_)),
	!,
	assert(stateBarman(STATE)).

addOrderReady(ID) :-
	assert(order(ID)).

removeOrderReady(ID) :-
	retract(order(ID)),
	!.



% STATE

statetearoom(stateWaiter(SW), stateBarman(SB), stateTable(1, ST1), stateTable(2, ST2)) :-
				stateWaiter(SW), stateBarman(SB), stateTable(1, ST1), stateTable(2, ST2).