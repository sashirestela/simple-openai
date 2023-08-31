{ pkgs }: {
    deps = [
        pkgs.nvi
        pkgs.gnupg
        pkgs.pinentry-curses
        pkgs.zip
        pkgs.graalvm11-ce
        pkgs.maven
        pkgs.replitPackages.jdt-language-server
        pkgs.replitPackages.java-debug
    ];
  services.pcscd.enable = true;
  programs.gnupg.agent = {
     enable = true;
     pinentryFlavor = "curses";
     enableSSHSupport = true;
  };
}