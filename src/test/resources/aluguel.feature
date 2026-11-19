# language: pt
Funcionalidade: Cadastro, estoque e aluguel de itens
  Como uma locadora ou cliente
  Quero cadastrar itens e solicitar aluguel sem pagamento
  Para controlar a disponibilidade dos equipamentos

  
  Cenário: Cadastrar uma empresa
    Dado que existe uma empresa cadastrada
    Então a empresa pode acessar seu cadastro

  
  Cenário: Cadastrar um item com manutenção
    Dado que existe uma empresa cadastrada
    Quando a empresa cadastra um item com 5 unidades e 1 em manutenção
    Então o estoque mostra 4 disponíveis

  
  Cenário: Reservar quantidade disponível
    Dado que existe uma empresa cadastrada
    E a empresa cadastra um item com 6 unidades e 1 em manutenção
    Quando o cliente solicita 3 unidades
    Então a locação contém 3 unidade ou unidades
    E o estoque mostra 2 disponíveis


  Cenário: Impedir locação acima do estoque
    Dado que existe uma empresa cadastrada
    E a empresa cadastra um item com 2 unidades e 0 em manutenção
    Quando o cliente tenta solicitar 3 unidades
    Então a solicitação é recusada por falta de estoque
    E o estoque mostra 2 disponíveis


  Cenário: Devolução repõe disponibilidade
    Dado que existe uma empresa cadastrada
    E a empresa cadastra um item com 4 unidades e 0 em manutenção
    E o cliente solicita 2 unidades
    Quando a empresa registra a devolução
    Então o estoque mostra 4 disponíveis


  Cenário: Alterar manutenção reduz o estoque
    Dado que existe uma empresa cadastrada
    E a empresa cadastra um item com 5 unidades e 1 em manutenção
    Quando a empresa altera a manutenção para 3 unidades
    Então o estoque mostra 2 disponíveis


  Cenário: Alugar itens de empresas diferentes no mesmo pedido
    Dado que existe uma empresa cadastrada
    E a empresa cadastra um item com 3 unidades e 0 em manutenção
    Quando o cliente solicita itens de duas empresas
    Então a locação apresenta 2 itens diferentes


  Cenário: Impedir edição por outra empresa
    Dado que existe uma empresa cadastrada
    E a empresa cadastra um item com 3 unidades e 0 em manutenção
    Quando outra empresa tenta alterar o item
    Então a alteração é negada


  Cenário: Evitar e-mails duplicados de empresas
    Dado que existe uma empresa cadastrada
    Quando se tenta cadastrar outra empresa com o mesmo e-mail
    Então o e-mail duplicado é rejeitado
