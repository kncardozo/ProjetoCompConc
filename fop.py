mapaAtual = []
erros = 0

def geraMapa(qtdAssentos):
    i=0
    while (i<qtdAssentos):
        mapaAtual.append(0)
        i+=1
    
def op1 (id, mapa):
    global erros
    if(mapa==mapaAtual):
        print("Thread %d visualizou o mapa de assentos" % id)
    else:
        erros+=1
        print("Problemas na operação 1 com a thread %d" % id)

def op2 (id, lugar, mapa):
    global erros
    mapaAtual[lugar-1]=id
    if(mapaAtual==mapa):
        print("Thread %d escolheu o assento livre %d" % (id, lugar))
    else:
        erros+=1
        print("Problemas na operação 2 com a thread %d" % id)
        
def op3 (id, lugar, mapa):
    global erros
    mapaAtual[lugar-1]=id
    if(mapaAtual==mapa):
        print("Thread %d selecionou o assento %d" % (id, lugar))
    else:
        erros+=1
        print("Problemas na operação 3 com a thread %d" % id)

def op4 (id, lugar, mapa):
    global erros
    mapaAtual[lugar-1]=0
    if(mapaAtual==mapa):
        print("Thread %d liberou o assento %d" % (id, lugar))
    else:
        erros+=1
        print("Problemas na operação 4 com a thread %d" % id)

def verificaCorretude ():
    global erros
    if(erros!=0):
        print("Programa principal inválido, com %d erros" % erros)
        
    else:
        print("Programa principal válido !")
    
