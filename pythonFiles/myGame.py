import pygame

pygame.init()

gameDisplay = pygame.display.set_mode((800,600))

pygame.display.set_caption('A bit Faster')

clock = pygame.time.Clock()

crashed = False

while not crashed:

	for event in pygame.event.get():
		if event.type == pygame.QUIT:
			crashed = True
			break

		print(event)

	pygame.display.update()

	clock.tick(30)

pygame.quit()
quit()

