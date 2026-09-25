 Imports System
 Imports System.IO
	
 ' Programa avanzado de ejemplo para análisis léxico
 Module ProyectoVB
    Sub Main()
        'Declaración de variables
        Dim numero1 As Integer = 7
        Dim numero2 As Double = 12.5
        Dim texto As String = "Resultado: "
        Dim esMayor As Boolean
	    Dim producto As Double

        'Suma y comparación
        Dim suma As Double = numero1 + numero2
        esMayor = suma > 15

        'Condición IF
        If esMayor Then
            Console.WriteLine(texto & "La suma es mayor que 15")
            
        Else
            Console.WriteLine(texto & "La suma es menor que 15")
        End If

        Dim contador As Integer = 0
        While contador < 3
            Console.WriteLine("Valor del contador: " & contador)
            contador = contador + 1
        End While

       'Llamada a función con parámetros
        	
        producto = Multiplicar(numero1, numero2)
        Console.WriteLine("El producto es: " & producto)

        ' Uso de operadores lógicos
        If esMayor And producto > 50 Then
            Console.WriteLine("La suma es mayor y el producto supera 50")
        End If
    End Sub

    ' Función para multiplicar dos números y adicionar un IF
    Function Multiplicar(a As Double, b As Double) As Double
        Return a * b


        Dim i As Integer
        For i = 1 To 3
            Console.WriteLine("Iteración For número: " & i)
        Next

    ' If/Else con suma y Console en cada bloque
    Dim edad As Integer = 17
    Dim puntos As Integer = 0

    f edad >= 18 Then
        puntos = puntos + 10       
        Console.WriteLine("Acceso concedido: mayor de edad.")
        Console.WriteLine("Puntos acumulados: " & puntos)
    Else
        puntos = puntos + 3           ' Suma
        Console.WriteLine("Acceso restringido: menor de edad.")
        Console.WriteLine("Puntos acumulados (limitados): " & puntos)
    End If


    End Function
End Module
