import Link from "next/link";
import React from "react";
import {
  NavigationMenuLink,
  NavigationMenu,
  NavigationMenuList,
  navigationMenuTriggerStyle,
} from "./ui/navigation-menu";
import { ModeToggle } from "./ModeToggle";

const tabs: { name: string; href: string }[] = [
  { name: "Home", href: "/" },
  { name: "Jobs", href: "/jobs" },
  { name: "Temps", href: "/temps" },
  { name: "Profile", href: "/profile" },
  { name: "Login", href: "/auth/login" },
];

const Navbar = () => {
  return (
    <NavigationMenu className="sticky top-0 left-0 w-full justify-end">
      <NavigationMenuList>
        {tabs.map((tab) => (
          <Link href={tab.href} legacyBehavior passHref key={tab.name}>
            <NavigationMenuLink>{tab.name}</NavigationMenuLink>
          </Link>
        ))}
        <ModeToggle />
      </NavigationMenuList>
    </NavigationMenu>
  );
};

export default Navbar;
