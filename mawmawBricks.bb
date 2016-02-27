AppTitle "mawmaw Bricks"

Graphics 1024,768,32,1
SetBuffer BackBuffer()
ClsColor 255,255,255
SeedRnd MilliSecs()

;paddle + ball
Global imgPaddleNormal     = LoadImage("bilder/paddle.bmp")
MidHandle(imgPaddleNormal)

Global imgPaddleSmall     = CopyImage(imgPaddleNormal)
ScaleImage imgPaddleSmall,0.5,1
MidHandle(imgPaddleSmall)

Global imgPaddleBig1     = CopyImage(imgPaddleNormal)
ScaleImage imgPaddleBig1,1.5,1
MidHandle(imgPaddleBig1)

Global imgPaddleBig2     = CopyImage(imgPaddleNormal)
ScaleImage imgPaddleBig2,2.0,1
MidHandle(imgPaddleBig2)

Global imgPaddleBig3     = CopyImage(imgPaddleNormal)
ScaleImage imgPaddleBig3,2.5,1
MidHandle(imgPaddleBig3)

Global imgBall       = LoadImage("bilder/ball.bmp")
MidHandle(imgBall)
MaskImage imgBall,255,255,255

Global imgGun       = LoadImage("bilder/gun.bmp")
MidHandle(imgGun)
MaskImage imgGun,255,255,255

Global imgShot      = LoadImage("bilder/Shot.bmp")
MidHandle(imgShot)
MaskImage imgShot,255,255,255

Dim bricks(4,3)

;bricks
Global imgBrickHard    = LoadImage("bilder/bricks/hard.bmp")    : bricks(0,0)=imgBrickHard
Global imgBrickBlue0   = LoadImage("bilder/bricks/blue1.bmp")   : bricks(1,0)=imgBrickBlue0
Global imgBrickBlue1   = LoadImage("bilder/bricks/blue2.bmp")   : bricks(1,1)=imgBrickBlue1
Global imgBrickBlue2   = LoadImage("bilder/bricks/blue3.bmp")   : bricks(1,2)=imgBrickBlue2
Global imgBrickBlue3   = LoadImage("bilder/bricks/blue4.bmp")   : bricks(1,3)=imgBrickBlue3
Global imgBrickRed0    = LoadImage("bilder/bricks/red1.bmp")    : bricks(2,0)=imgBrickRed0
Global imgBrickRed1    = LoadImage("bilder/bricks/red2.bmp")    : bricks(2,1)=imgBrickRed1
Global imgBrickRed2    = LoadImage("bilder/bricks/red3.bmp")    : bricks(2,2)=imgBrickRed2
Global imgBrickRed3    = LoadImage("bilder/bricks/red4.bmp")    : bricks(2,3)=imgBrickRed3
Global imgBrickGreen0  = LoadImage("bilder/bricks/green1.bmp")  : bricks(3,0)=imgBrickGreen0
Global imgBrickGreen1  = LoadImage("bilder/bricks/green2.bmp")  : bricks(3,1)=imgBrickGreen1
Global imgBrickGreen2  = LoadImage("bilder/bricks/green3.bmp")  : bricks(3,2)=imgBrickGreen2
Global imgBrickGreen3  = LoadImage("bilder/bricks/green4.bmp")  : bricks(3,3)=imgBrickGreen3
Global imgBrickYellow0 = LoadImage("bilder/bricks/yellow1.bmp") : bricks(4,0)=imgBrickYellow0
Global imgBrickYellow1 = LoadImage("bilder/bricks/yellow2.bmp") : bricks(4,1)=imgBrickYellow1
Global imgBrickYellow2 = LoadImage("bilder/bricks/yellow3.bmp") : bricks(4,2)=imgBrickYellow2
Global imgBrickYellow3 = LoadImage("bilder/bricks/yellow4.bmp") : bricks(4,3)=imgBrickYellow3


;fullscreen
Global imgGameOver=LoadImage("bilder/GameOver.jpg")
Global imgGameWin = LoadImage("bilder/GameWin.jpg")

;items
Global itmX2=LoadImage("bilder/items/itmX2.jpg")
MaskImage itmX2,255,255,255
Global itmThrough=LoadImage("bilder/items/itmThrough.jpg")
MaskImage itmThrough,255,255,255
Global itmLife=LoadImage("bilder/items/itmLife.jpg")
MaskImage itmLife,255,255,255
Global itmSticky=LoadImage("bilder/items/itmSticky.jpg")
MaskImage itmSticky,255,255,255
Global itmFinish=LoadImage("bilder/items/itmFinish.jpg")
MaskImage itmFinish,255,255,255
Global itmExpand=LoadImage("bilder/items/itmExpand.jpg")
MaskImage itmExpand,255,255,255
Global itmShrink=LoadImage("bilder/items/itmShrink.jpg")
MaskImage itmShrink,255,255,255
Global itmDeath=LoadImage("bilder/items/itmDeath.jpg")
MaskImage itmDeath,255,255,255
Global itmShoot=LoadImage("bilder/items/itmShoot.jpg")
MaskImage itmShoot,255,255,255

;variables
MoveMouse 512,0
Global paddleWidth%=60
Global remBalls%=3
Global currLevel%=-1
Global brickCount%=0
Global throughMode%=0
Global shootMode%=0
Global m1hit%, m1down%, m2hit%, m2down%
Global paddleSize%=1 ;0=Small, 1=Normal, 2=Big1, 3=Big2, 4=Big3
Global paddleX%

Type ball
	Field xPos#
	Field yPos#
	Field vx#
	Field vy#
	Field held%=0
	Field offset%
End Type

Type brick
	Field xPos
	Field yPos
	Field bType
	Field level
End Type

Type item
	Field xPos#
	Field yPos#
	Field vx#
	Field vy#
	Field itmType
End Type

Type shot
	Field xPos
	Field yPos
End Type 


ba.ball = New ball
ba\xPos= paddleX+10
ba\yPos= 748
ba\vx  = 0
ba\vy  = 0
ba\held=1

Include "levels.bb"

For x=0 To 15
	For y=0 To 15
		For z=0 To 9
			If levels(x,y,z)>0 Then
				br.brick=New brick
				br\xPos=x*64
				br\yPos=y*32
				br\bType=levels(x,y,z)
				br\level=z
			EndIf
		Next
	Next
Next

frametimer=CreateTimer(50)
nextLevel()
While Not KeyHit(1)
	WaitTimer frametimer
	
	m1hit= MouseHit(1)
	m1down=MouseDown(1)
	m2hit= MouseHit(2)
	m2down=MouseDown(2)
	
	;bricks	
	For x=0 To 15
		For y=0 To 15
			If levels(x,y,currLevel)=1  DrawBlock bricks(0,0),x*64,y*32
			
			If levels(x,y,currLevel)=11 DrawBlock bricks(1,0),x*64,y*32
			If levels(x,y,currLevel)=12 DrawBlock bricks(1,1),x*64,y*32
			If levels(x,y,currLevel)=13 DrawBlock bricks(1,2),x*64,y*32
			If levels(x,y,currLevel)=14 DrawBlock bricks(1,3),x*64,y*32
			
			If levels(x,y,currLevel)=21 DrawBlock bricks(2,0),x*64,y*32
			If levels(x,y,currLevel)=22 DrawBlock bricks(2,1),x*64,y*32
			If levels(x,y,currLevel)=23 DrawBlock bricks(2,2),x*64,y*32
			If levels(x,y,currLevel)=24 DrawBlock bricks(2,3),x*64,y*32
			
			If levels(x,y,currLevel)=31 DrawBlock bricks(3,0),x*64,y*32
			If levels(x,y,currLevel)=32 DrawBlock bricks(3,1),x*64,y*32
			If levels(x,y,currLevel)=33 DrawBlock bricks(3,2),x*64,y*32
			If levels(x,y,currLevel)=34 DrawBlock bricks(3,3),x*64,y*32
			
			If levels(x,y,currLevel)=41 DrawBlock bricks(4,0),x*64,y*32
			If levels(x,y,currLevel)=42 DrawBlock bricks(4,1),x*64,y*32
			If levels(x,y,currLevel)=43 DrawBlock bricks(4,2),x*64,y*32
			If levels(x,y,currLevel)=44 DrawBlock bricks(4,3),x*64,y*32
		Next
	Next
	
	
	;paddle
	paddleX=MouseX()
	If paddleX>1024-paddleWidth paddleX=1024-paddleWidth
	If paddleX<0+paddleWidth paddleX=0+paddleWidth
	
	If paddleWidth= 30 DrawImage imgPaddleSmall,paddleX,758  : imgPaddle=imgPaddleSmall
	If paddleWidth= 60 DrawImage imgPaddleNormal,paddleX,758 : imgPaddle=imgPaddleNormal
	If paddleWidth= 90 DrawImage imgPaddleBig1,paddleX,758   : imgPaddle=imgPaddleBig1
	If paddleWidth=120 DrawImage imgPaddleBig2,paddleX,758   : imgPaddle=imgPaddleBig2
	If paddleWidth=150 DrawImage imgPaddleBig3,paddleX,758   : imgPaddle=imgPaddleBig3
	
	If shootMode=1 Then
		DrawImage imgGun,paddleX-paddleWidth,758
		DrawImage imgGun,paddleX+paddleWidth,758
		
		If m1hit=1 shoot()
	EndIf
	
	If m1hit Then
		For ba.ball=Each ball
			ba\held=0
		Next
	EndIf
	
	;balls
	For ba.ball=Each ball
		If MouseHit(1) And ba\held=1 Then
			ba\held=0
			ba\vy=-10
		EndIf
		
		If ba\held=0 Then
			If ImagesOverlap(imgBall, ba\xPos, ba\yPos, imgPaddle, paddleX, 758) Then
				vvorher#=Sqr(ba\vx^2+ba\vy^2)
				unterschied% = ba\xPos-paddleX
				ba\vx=ba\vx+unterschied/3
				If ba\vx> 10 ba\vx= 10
				If ba\vx<-10 ba\vx=-10
				ba\vy=Abs(ba\vy)*-1
				If holdMode=1 Then
					ba\held=1
					ba\offset=ba\xPos-paddleX
				EndIf
			EndIf
			
			
			For br.brick=Each brick
				If br\level=currLevel Then
					If ImageRectCollide(imgBall,ba\xPos,ba\yPos,0,br\xPos,br\yPos,64,32) Then						
						If throughMode=0 Then
						
							If ba\xPos<br\xPos Then
								If ba\vx>0 ba\vx=ba\vx*-1 ba\xPos=ba\xPos-10
							EndIf
							If ba\xPos>br\xPos+64 Then
								If ba\vx<0 ba\vx=ba\vx*-1 ba\xPos=ba\xPos+10
							EndIf
							If ba\yPos<br\yPos    Then
								If ba\vy>0 ba\vy=ba\vy*-1 ba\yPos=ba\yPos-10
							EndIf
							If ba\yPos>br\yPos+32 Then
								If ba\vy<0 ba\vy=ba\vy*-1 ba\yPos=ba\yPos+10
							EndIf
						EndIf
				
						hitBrick(br\xPos/64,br\yPos/32)
						If levels(br\xPos/64,br\yPos/32,currLevel)<1 Then
							Delete br.brick
							brickCount=brickCount-1
						EndIf
				
					EndIf
				EndIf
			Next
			
			ba\xPos=ba\xPos+ba\vx
			ba\yPos=ba\yPos+ba\vy
			DrawImage imgBall,ba\xPos,ba\yPos
			
		Else
			ba\xPos=paddleX+ba\offset
			DrawImage imgBall,ba\xPos,741
		EndIf
			
		If ba\xPos<10   ba\vx=Abs(ba\vx)
		If ba\xPos>1014 ba\vx=Abs(ba\vx)*-1
		If ba\yPos<10   ba\vy=Abs(ba\vy)
		If ba\yPos>768 lostBall()
	Next
	
	countBricks()
	If brickCount=0 nextLevel()
	
	;items
	For itm.item=Each item
	
		itm\vy=itm\vy+0.1
		itm\xPos=itm\xPos+itm\vx
		itm\yPos=itm\yPos+itm\vy
		
		If itm\xPos<10      itm\vx=Abs(itm\vx)
		If itm\xPos>1024-64 itm\vx=Abs(itm\vx)*-1
		
		If itm\itmType=0 DrawImage itmX2,itm\xPos,itm\yPos
		If itm\itmType=1 DrawImage itmThrough,itm\xPos,itm\yPos
		If itm\itmType=2 DrawImage itmLife,itm\xPos,itm\yPos
		If itm\itmType=3 DrawImage itmSticky,itm\xPos,itm\yPos
		If itm\itmType=4 DrawImage itmFinish,itm\xPos,itm\yPos
		If itm\itmType=5 DrawImage itmExpand,itm\xPos,itm\yPos
		If itm\itmType=6 DrawImage itmShrink,itm\xPos,itm\yPos
		If itm\itmType=7 DrawImage itmDeath,itm\xPos,itm\yPos
		If itm\itmType=8 DrawImage itmShoot,itm\xPos,itm\yPos
		
		If ImageRectOverlap (imgPaddle,paddleX,758,itm\xPos,itm\yPos,64,64) Then
			If itm\itmType=0 doubleBalls()
			If itm\itmType=1 throughMode=1
			If itm\itmType=2 remBalls=remBalls+1
			If itm\itmType=3 holdMode=1 
			If itm\itmType=4 nextLevel() : Exit
			If itm\itmType=5 expandPaddle()
			If itm\itmType=6 shrinkPaddle()
			If itm\itmType=7 catchDeath()
			If itm\itmType=8 shootMode=1
			
			Delete itm
		EndIf
		
	Next
	
	;shots
	For sh.shot=Each shot
		sh\yPos=sh\yPos-10
		DrawImage imgShot,sh\xPos,sh\yPos
		For br.brick=Each brick
			If br\level=currLevel Then
				If ImageRectOverlap(imgShot,sh\xPos,sh\yPos,br\xPos,br\yPos,64,32) Then		
					hitBrick(br\xPos/64,br\yPos/32)	
					
					If levels(br\xPos/64,br\yPos/32,currLevel)<1 Then
						Delete br.brick
						brickCount=brickCount-1
					EndIf
								
					Delete sh.shot	
					Exit		
				EndIf
			EndIf
		Next
	Next
	
	Color 0,0,0
	Text 1000,0,remBalls
	Flip 1
	Cls
Wend
End




Function lostBall()

	For ba.ball=Each ball
		If ba\yPos>768 Delete ba
	Next
	
	For ba.ball=Each ball
		currBalls=currBalls+1
	Next
	
	throughMode=0
	holdMode=0
	
	If currBalls=0
		remBalls=remBalls-1
		If remBalls<0 gameOver()
	
		ba.ball = New ball
		ba\xPos= paddleX+10
		ba\yPos= 748
		ba\vx  = 3
		ba\vy  = -10
		ba\held=1
	EndIf
	currBalls=0
End Function

Function gameOver()
	DrawBlock imgGameOver,0,0
	Flip
	FlushMouse()
	WaitMouse()
	End
End Function

Function gameWin()
	DrawBlock imgGameWin,0,0
	Flip
	FlushMouse()
	WaitMouse()
	End
End Function

Function nextLevel()
	
	For br.brick=Each brick
		If br\level=currLevel Delete br.brick
	Next
	
	currLevel=currlevel+1
	If currLevel>9 gameWin()
	
	countBricks()
	
	For ba.ball = Each ball
		Delete ba.ball
	Next
	
	For it.item=Each item
		Delete it.item
	Next
	
	throughMode=0
	holdMode=0
	shootMode=0
	
	ba.ball = New ball
	ba\xPos= paddleX+10
	ba\yPos= 748
	ba\vx  = 3
	ba\vy  = -10
	ba\held=1
End Function

Function countBricks()
	brickCount=0
	For x=0 To 15
		For y=0 To 15
			If levels(x,y,currLevel)=1 Or levels(x,y,currLevel)>2 brickCount=brickCount+1
		Next
	Next
End Function

Function hitBrick(x,y)
	
	brickDestroyed=0
	If levels(x,y,currLevel)>9
		If levels(x,y,currLevel) Mod 10 = 1
			brickDestroyed=1
			levels(x,y,currLevel)=0
		ElseIf levels(x,y,currLevel) Mod 10 = 2 
			levels(x,y,currLevel)=levels(x,y,currLevel)-1
		ElseIf levels(x,y,currLevel) Mod 10 = 3
			levels(x,y,currLevel)=levels(x,y,currLevel)-1
		ElseIf levels(x,y,currLevel) Mod 10 = 4
			levels(x,y,currLevel)=levels(x,y,currLevel)-1
		EndIf
	EndIf

	
	
	;items
	If brickDestroyed=1 Then
		random=Rand(0,10)
		If random=0 createItem(x,y) 
		brickDestroyed=0
	EndIf
	
End Function

Function createItem(x,y)
	
	random=Rand(0,100)
	itm.item=New item
	itm\xPos=x*64
	itm\yPos=y*32
	itm\vx=Rand(-10,10)
	itm\vy=Rand(-5,0)
	
	Select True
		Case random>-1 And random < 10 tType=0
		Case random=> 10 And random < 20 tType=1
		Case random=> 20 And random < 25 tType=2
		Case random=> 25 And random < 35 tType=3
		Case random=> 35 And random < 40 tType=4
		Case random=> 40 And random < 60 tType=5
		Case random=> 60 And random < 80 tType=6
		Case random=> 80 And random <100 tType=7
		Case random=>100 And random <120 tType=8
	End Select
	
	itm\itmType=tType
	
	
End Function 

Function doubleBalls()
	ballCount%=0
	For ba.ball=Each ball
		ballCount=ballCount+1
	Next
	
	For ba.ball=Each ball
		
		ba1.ball=New ball
		ba1\xPos = ba\xPos
		ba1\yPos = ba\yPos
		ba1\vx   = ba\vx+Rnd(-2,2)
		ba1\vy   = ba\vy+Rnd(-2,2)
		
		ballCount=ballCount-1
		If ballCount=0 Exit
	Next
	
End Function

Function catchDeath()
	For ba.ball=Each ball
		Delete ba.ball
	Next
	lostBall()
End Function

Function expandPaddle()
	If paddleWidth<150 Then
		paddleWidth=paddleWidth+30
	EndIf
End Function

Function shrinkPaddle()
	If paddleWidth>30 Then
		paddleWidth=paddleWidth-30
	EndIf
End Function

Function shoot()
	sh.shot=New shot
	sh\xPos= paddleX-paddleWidth
	sh\yPos=748
	sh.shot=New shot
	sh\xPos= paddleX+paddleWidth
	sh\yPos=748
End Function