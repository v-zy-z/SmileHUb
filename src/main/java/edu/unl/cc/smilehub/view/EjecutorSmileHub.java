package edu.unl.cc.smilehub.view;

import java.util.Scanner;
import edu.unl.cc.smilehub.domain.AtencionMedica;
import edu.unl.cc.smilehub.domain.TipoAtencion;


public class EjecutorSmileHub {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AtencionMedica atencion = new AtencionMedica();

        System.out.println("<----- SISTEMA AGENDAMIENTO CITAS SMILEHUB ----->");
        System.out.println("1. Agendar Atención Médica");
        System.out.println("2. Salir");
        System.out.print("Seleccione una opción: ");

        int option = sc.nextInt();
        sc.nextLine();

        switch (option) {
            case 1:
                System.out.print("Ingrese el nombre de la atención médica: ");
                String nombre = sc.nextLine();

                System.out.print("Ingrese el costo de la atención: ");
                double costo = sc.nextDouble();
                sc.nextLine();

                System.out.print("Ingrese la descripción médica: ");
                String descripcion = sc.nextLine();

                System.out.println("Seleccione el tipo de atención médica:");
                for (TipoAtencion t : TipoAtencion.values()) {
                    System.out.println("- " + t);
                }

                System.out.print("Ingrese el tipo de atención (exacto): ");
                String tipoTexto = sc.nextLine();

                TipoAtencion tipo = TipoAtencion.valueOf(tipoTexto);

                System.out.println("\nAtención médica registrada correctamente:");
                System.out.println(atencion);
                break;

            case 2:
                System.out.println("Saliendo del sistema...");
                break;

            default:
                System.out.println("Opción no válida");
        }

        sc.close();
    }
}