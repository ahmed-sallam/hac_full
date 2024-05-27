import {Routes} from '@angular/router';
import {DashHomeComponent} from "./dashboard/dash-home/dash-home.component";
import {AuthGuard} from "./login/auth.guard";

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch:'full'
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./dashboard/dashboard.component').then((c)=>c.DashboardComponent),
    children: [
      {
        path:'',
        component: DashHomeComponent
      },
      {
        path:'core',
        loadComponent: () => import('./dashboard/core/core.component').then((c)=>c.CoreComponent)
      },
      {
        path:'core/countries',
        loadComponent: () => import('./dashboard/core/countries/countries.component').then((c)=>c.CountriesComponent)
      },
      {
        path:'inventory',
        loadComponent: () => import('./dashboard/inventory/inventory.component').then((c)=>c.InventoryComponent)
      },
      {
        path:'inventory/stock',
        loadComponent: () => import('./dashboard/inventory/stock/stock.component').then((c)=>c.StockComponent)
      },
      {
        path:'inventory/stock/create',
        loadComponent: () => import('./dashboard/inventory/stock/create-stock/create-stock.component').then((c)=>c.CreateStockComponent)
      },
      {
        path:'inventory/stores',
        loadComponent: () => import('./dashboard/inventory/stores/stores.component').then((c)=>c.StoresComponent)
      },
      {
        path: 'inventory/stores/create',
        loadComponent: () => import('./dashboard/inventory/stores/create-store/create-store.component').then((c) => c.CreateStoreComponent)
      },
      {
        path: 'inventory/stores/:id',
        loadComponent: () => import('./dashboard/inventory/stores/one-store/one-store.component').then((c) => c.OneStoreComponent)
      },
      {
        path:'inventory/brands',
        loadComponent: () => import('./dashboard/inventory/brands/brands.component').then((c)=>c.BrandsComponent)
      },
      {
        path:'inventory/brands/create',
        loadComponent: () => import('./dashboard/inventory/brands/create-brand/create-brand.component').then((c)=>c.CreateBrandComponent)
      },
      {
        path:'inventory/brands/:id',
        loadComponent: () => import('./dashboard/inventory/brands/one-brand/one-brand.component').then((c)=>c.OneBrandComponent)
      },
      {
        path:'inventory/machine-parts',
        loadComponent: () => import('./dashboard/inventory/machine-parts/machine-parts.component').then((c)=>c.MachinePartsComponent)
      },
      {
        path:'inventory/machine-parts/create',
        loadComponent: () => import('./dashboard/inventory/machine-parts/create-parts/create-parts.component').then((c)=>c.CreatePartsComponent)
      },
      {
        path:'inventory/machine-parts/:id',
        loadComponent: () => import('./dashboard/inventory/machine-parts/one-part/one-part.component').then((c)=>c.OnePartComponent)
      },
      {
        path:'inventory/products',
        loadComponent: () => import('./dashboard/inventory/products/products.component').then((c)=>c.ProductsComponent)
      },
      {
        path:'inventory/products/create',
        loadComponent: () => import('./dashboard/inventory/products/create-product/create-product.component').then((c)=>c.CreateProductComponent)
      },
      {
        path:'inventory/products/:id',
        loadComponent: () => import('./dashboard/inventory/products/one-product/one-product.component').then((c)=>c.OneProductComponent)
      },
      {
        path:'inventory/machinery',
        loadComponent: () => import('./dashboard/inventory/machinery/machinery.component').then((c)=>c.MachineryComponent)
      },
      {
        path:'inventory/machinery/create',
        loadComponent: () => import('./dashboard/inventory/machinery/create-machine/create-machine.component').then((c)=>c.CreateMachineComponent)
      },
      {
        path:'inventory/machinery/:id',
        loadComponent: () => import('./dashboard/inventory/machinery/one-machine/one-machine.component').then((c)=>c.OneMachineComponent)
      },
      {
        path:'inventory/replenishment',
        loadComponent: () => import('./dashboard/inventory/replenishment/replenishment.component').then((c)=>c.ReplenishmentComponent)
      },
      {
        path:'sales',
        loadComponent: () => import('./dashboard/sales/sales.component').then((c)=>c.SalesComponent)
      },
      {
        path:'purchases',
        loadComponent: () => import('./dashboard/purchases/purchases.component').then((c)=>c.PurchasesComponent)
      },
      {
        path:'purchases/material-requests',
        loadComponent: () => import('./dashboard/purchases/material-request/material-request.component').then((c)=>c.MaterialRequestComponent)
      },
      {
        path:'purchases/material-requests/:id',
        loadComponent: () => import('./dashboard/purchases/material-request/one-material-request/one-material-request.component').then((c)=>c.OneMaterialRequestComponent)
      },
      {
        path:'purchases/material-requests/:id/edit',
        loadComponent: () => import('./dashboard/purchases/material-request/edit-material-request/edit-material-request.component').then((c)=>c.EditMaterialRequestComponent)
      },
    ],
    canActivate:[AuthGuard]
  },
  {
    path:'login',
    loadComponent: () => import('./login/login.component').then((c)=>c.LoginComponent)
  },
  {
    path:'**',
    redirectTo: 'dashboard'
  }
];
