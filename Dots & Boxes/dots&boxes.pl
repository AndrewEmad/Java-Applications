run(State,MaxLevel,BestState,Desired):- %input: State, MaxLevel / Output: Location, Desired
    max(State,((_,BestState),Value),1,MaxLevel),!,
    waive(Value, Desired).
    %updateScore(OldScore,Value,NewScore).

waive(Value,Desired):- %Tells whether the agent wants to play or waive
    Value>=0,!,
    Desired is 1.
waive(_,0). %Not Desired and I want to waive
/*
updateScore(OldScore,Value,NewScore):-
    Value > 0,
    Value mod 4 =:= 0,!,
    NewScore is OldScore + 1.
updateScore(OldScore,_,OldScore).*/

min((AffectedBoxes,State),((ChildAffectedBoxes,Child),Value),_,1):-
    finalTurn(State),!,
    moves((AffectedBoxes,State),[(ChildAffectedBoxes,Child)]),
    utility(Child,ChildAffectedBoxes,Value).
min((AffectedBoxes,State),((AffectedBoxes,State),Value),_,_):-
    finalTurn(State),!,
    moves((AffectedBoxes,State),[(ChildAffectedBoxes,Child)]),
    utility(Child,ChildAffectedBoxes,Value).

min((AffectedBoxes,State),(WorstState,WorstValue),L,L):-
    !,moves((AffectedBoxes,State),Children),
	bestState(Children,WorstState,WorstValue).

min(State,(MinState,MinValue),CurrentLevel,MaxLevel):-
    moves(State,Children),
    maxList(Children,ValuedChildren,CurrentLevel,MaxLevel),
    minimum(ValuedChildren,(MinState,MaxValue)), % the most harmful state form max is the most beneficial state for min
    MinValue is MaxValue * -1.

max((AffectedBoxes,State),((ChildAffectedBoxes,Child),Value),_,1):-
    finalTurn(State),!,
    moves((AffectedBoxes,State),[(ChildAffectedBoxes,Child)]),
    utility(Child,ChildAffectedBoxes,Value).
max((AffectedBoxes,State),((AffectedBoxes,State),Value),_,_):-
    finalTurn(State),!,
    moves((AffectedBoxes,State),[(ChildAffectedBoxes,Child)]),
    utility(Child,ChildAffectedBoxes,Value).

max((AffectedBoxes,State),(BestState,BestValue),L,L):-
	!,moves((AffectedBoxes,State),Children),
	bestState(Children,BestState,BestValue).

max(State,(MaxState,MaxValue),CurrentLevel,MaxLevel):-
    moves(State,Children),
    minList(Children,ValuedChildren,CurrentLevel,MaxLevel), %output: ListOfMin
    minimum(ValuedChildren,(MaxState,MinValue)), % the most harmful state form min is the most beneficial state for max
    MaxValue is MinValue * -1.

minList([],[],_,_).
minList([State|Tail],[(State,Value)|R],CurrentLevel,MaxLevel):-
    C is CurrentLevel + 1,
    min(State,(_,Value),C,MaxLevel),
    minList(Tail,R,CurrentLevel,MaxLevel).

maximum([H],H).
maximum([(State,Value)|Tail],(SR,VR)):-
    maximum(Tail,(State1,Value1)),
    maxOf2((State,Value),(State1,Value1),(SR,VR)).

maxOf2((State,Value),(_,Value1),(SR,VR)):-
    Value>Value1,!,
    SR = State,
    VR is Value.

maxOf2((_,Value),(State1,Value1),(SR,VR)):-
    Value1>=Value,
    SR = State1,
    VR is Value1.


maxList([],[],_,_).
maxList([State|Tail],[(State,Value)|R],CurrentLevel,MaxLevel):-
    C1 is CurrentLevel+1,
    max(State,(_,Value),C1,MaxLevel),
    maxList(Tail,R,CurrentLevel,MaxLevel).

minimum([H],H).
minimum([(S,V)|T],(SR,VR)):-
    minimum(T,(S1,V1)),
    minof2((S,V),(S1,V1),(SR,VR)).

minof2((S,V),(_,V1),(Sm,Vm)):-
    V=<V1,!,
    Sm = S,
    Vm is V.

minof2((_,V),(S1,V1),(Sm,Vm)):-
    V1<V,
    Sm = S1,
    Vm is V1.

bestState([(AffectedBoxes,Child)],(AffectedBoxes,Child),Value):- !,
	utility(Child,AffectedBoxes,Value).
bestState([(AffectedBoxes,Child)|OtherChildren],(AffectedBoxes,Child),Value):-
	bestState(OtherChildren,_,CurrValue),
	utility(Child,AffectedBoxes,Value),
	Value >= CurrValue, !.
	
bestState([_|OtherChildren],State,Value):-
	bestState(OtherChildren,State,Value).

worstState([(AffectedBoxes,Child)],(AffectedBoxes,Child),Value):- !,
	utility(Child,AffectedBoxes,Value).
worstState([(AffectedBoxes,Child)|OtherChildren],(AffectedBoxes,Child),Value):-
	worstState(OtherChildren,_,CurrValue),
	utility(Child,AffectedBoxes,Value),
	Value =< CurrValue, !.
	
worstState([_|OtherChildren],State,Value):-
	worstState(OtherChildren,State,Value).

moves((_,State),CleanChildren):-
	count(State,NumOfBoxes),
    getChildren([],State,NumOfBoxes,DirtyChildren),
    rem_duplicates(DirtyChildren, CleanChildren).


getChildren(_,[],_,[]):- !.
getChildren(PrevBoxes,[Box|NxtBoxes],NumOfBoxes,Children):-
    count(PrevBoxes,L),
    BoxIndex is L+1,
    Width is floor(sqrt(NumOfBoxes)),
    bagof([X,AffectedSide],getChild(Box,X,AffectedSide,BoxIndex,Width),BoxBag),!, %Width
    generateChildren(PrevBoxes,BoxBag,NxtBoxes,Width,BoxChildren),
    append(Box,PrevBoxes,NewPrevBoxes),
    getChildren(NewPrevBoxes,NxtBoxes,NumOfBoxes,OtherChildren),
    conc(BoxChildren,OtherChildren,Children).
getChildren(PrevBoxes,[Box|NxtBoxes],NumOfBoxes,Children):-
	append(Box,PrevBoxes,NewPrevBoxes),
    getChildren(NewPrevBoxes,NxtBoxes,NumOfBoxes,Children).

generateChildren(PrevBoxes,[[([AffectedBox1,AffectedBox2],Box),AffectedSide]],NxtBoxes,Width,[([AffectedBox1,AffectedBox2],NewChild)]):-
    !,append(Box,PrevBoxes,NewPrevBoxes),
    conc(NewPrevBoxes,NxtBoxes,Child),
    updateAffectedBox2(Child,AffectedSide,1,AffectedBox2,Width,NewChild).

generateChildren(PrevBoxes,[[([AffectedBox1,AffectedBox2],Box),AffectedSide]|OtherBoxes],NxtBoxes,Width,Children):- %given all alternatives for ONE box, put each alternative in a child state
    generateChildren(PrevBoxes,OtherBoxes,NxtBoxes,Width,OtherChildren),
    append(Box,PrevBoxes,NewPrevBoxes),
    conc(NewPrevBoxes,NxtBoxes,Child),
    updateAffectedBox2(Child,AffectedSide,1,AffectedBox2,Width,NewChild),
    append(([AffectedBox1,AffectedBox2],NewChild),OtherChildren,Children).

getChild([L,R,T,B],([BoxIndex,CorrectBox],[NewL,R,T,B]),"r",BoxIndex,Width):-%affects left box
    L =:= 0,
    NewL is L+1,
    AffectedBox is BoxIndex - 1,
    checkIfBounded(AffectedBox,Width,CorrectBox).

getChild([L,R,T,B],([BoxIndex,CorrectBox],[L,NewR,T,B]),"l",BoxIndex,Width):-%affects right box
    R =:= 0,
    NewR is R+1,
    AffectedBox is BoxIndex +1,
    checkIfBounded(AffectedBox,Width,CorrectBox).

getChild([L,R,T,B],([BoxIndex,CorrectBox],[L,R,NewT,B]),"b",BoxIndex,Width):-%affects top box
    T =:= 0,
    NewT is T+1,
    AffectedBox is BoxIndex - Width,
    checkIfBounded(AffectedBox,Width,CorrectBox).

getChild([L,R,T,B],([BoxIndex,CorrectBox],[L,R,T,NewB]),"t",BoxIndex,Width):-%affects bottom box
    B =:= 0,
    NewB is B+1,
    AffectedBox is BoxIndex + Width,
    checkIfBounded(AffectedBox,Width,CorrectBox).

checkIfBounded(BoxIndex,Width,BoxIndex):-
	BoxIndex =< Width * Width,!.
checkIfBounded(_,_,0).

updateAffectedBox2(State,_,_,AffectedBox,_,State):-%out of bound
    AffectedBox =< 0,!.
updateAffectedBox2(State,_,_,AffectedBox,Width,State):-%out of bound
	NumOfBoxes is Width * Width,
    AffectedBox > NumOfBoxes,!.
updateAffectedBox2(State,"l",_,AffectedBox,Width,State):-%out of bound
    (AffectedBox - 1) mod Width =:= 0,!.
updateAffectedBox2(State,"r",_,AffectedBox,Width,State):-%out of bound
    AffectedBox mod Width =:= 0,!.

updateAffectedBox2([Box|T],Side,BoxIndex,BoxIndex,_,[NewBox|T]):-
    updateSide(Box,Side,NewBox),!.
updateAffectedBox2([Box|T],Side,CurrentBox,AffectedBox,Width,[Box|NewT]):-
    NextBox is CurrentBox +1,
    updateAffectedBox2(T,Side,NextBox,AffectedBox,Width,NewT).

updateSide([L,_,T,B],Side,[L,1,T,B]):-
    Side =:= "r",!.
updateSide([_,R,T,B],Side,[1,R,T,B]):-
    Side =:= "l",!.
updateSide([L,R,_,B],Side,[L,R,1,B]):-
    Side =:= "t",!.
updateSide([L,R,T,_],Side,[L,R,T,1]):-
    Side =:= "b",!.

utility(State,[FirstBox,SecondBox],Value):-
    getNumberOfLines(State,1,FirstBox,NumberOfLines1),
    getNumberOfLines(State,1,SecondBox,NumberOfLines2),
    getValue(NumberOfLines1,NumberOfLines2,Value).

getNumberOfLines(_,_,AffectedBox,0):-
    AffectedBox =:= 0,!.

getNumberOfLines([[L,R,T,B]|_],BoxNumber,AffectedBox,NumberOfLines):-
    BoxNumber =:= AffectedBox,!,
    NumberOfLines is L+R+T+B.

getNumberOfLines([_|T],BoxNumber,AffectedBox,NumberOfLines):-
    getNumberOfLines(T,BoxNumber+1,AffectedBox,NumberOfLines).

getValue(Box1Lines,Box2Lines,Value):-
    Box1Lines \= 3,
    Box2Lines \= 3,!,
    Value is Box1Lines+Box2Lines-1.

getValue(Box1Lines,Box2Lines,Value):-
    Box1Lines \= 4,
    Box2Lines \= 4,!,
    Value is Box1Lines * -1 + Box2Lines * -1.

getValue(Box1Lines,Box2Lines,Value):-
    Value is Box1Lines+Box2Lines-1.

ifTerminal([]):- !.

ifTerminal([[L,R,T,B]|Tail]):-
    L =:= 1,
    R =:= 1,
    T =:= 1,
    B =:= 1,
    ifTerminal(Tail).

finalTurn(State):-
    countZeros(State,0,NumOfZeros),
    NumOfZeros =< 1.

countZeros([],NumOfZeros,NumOfZeros):-!.
countZeros([Box|T],Count,NumOfZeros):-
    countBox(Box,ZerosInBox),
    NewCount is Count + ZerosInBox,
    countZeros(T,NewCount,NumOfZeros).

countBox([L,R,T,B],Res):-
    Res is 4 - (L + R + T + B).

count(L,N):- %counts the length of a list
    len(L,0,N).
len([],N,N).
len([_|L],N,Ln):-
    N1 is N+1,
    len(L,N1,Ln).

conc([],A,A):- !.
conc([X|A],B,[X|C]) :- conc(A,B,C).

append(X,[],[X]):- !.
append(X,[H|T],[H|R]):-
    append(X,T,R).

rem_duplicates([], []).
rem_duplicates([X|Xs], Ys):-
	member(X, Xs),!,
	rem_duplicates(Xs, Ys).
rem_duplicates([X|Xs], [X|Ys]):-
	rem_duplicates(Xs, Ys).

member(_,[]):- fail,!.
member((_,X),[(_,X)|_]):-!.
member(X,[_|Tail]) :- member(X,Tail).