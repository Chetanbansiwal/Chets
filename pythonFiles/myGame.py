import pygame

pygame.init()
dwidth = 800
dheight = 600

black = (0,0,0)
white = (255,255,255)
red = (255,0,0)
green = (0,255,0)
blue = (0,0,255)


gameDisplay = pygame.display.set_mode((dwidth,dheight))

pygame.display.set_caption('A bit Faster')

clock = pygame.time.Clock()

carImg = pygame.image.load('car.png')

def car(x,y):
	gameDisplay.blit(carImg,(x,y))


def game_loop():
	x = (dwidth*0.45)
	y = (dheight*0.8)
	x_change = 0

	gameEnd = False

	while not gameEnd:

		for event in pygame.event.get():
			if event.type == pygame.QUIT:
				crashed = True

			if event.type == pygame.KEYDOWN:
				if event.key == pygame.K_LEFT:
					if x >= 0:
						x_change = -5

				elif event.key == pygame.K_RIGHT:
					if x <= dwidth:
						x_change = 5

			elif event.type == pygame.KEYUP:
				x_change = 0



		x += x_change
		gameDisplay.fill(white)
		car(x,y)
		pygame.display.update()

		clock.tick(30)

game_loop()

pygame.quit()
quit()

